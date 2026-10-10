# Releasing springdoc-openapi

The release itself is driven by [`.github/workflows/release.yml`](release.yml), run from the branch
being released (`main` or `spring-boot-3`) with **Actions → Release → Run workflow**.
`spring-boot-3` predates the MCP modules, so the npm and dashboard steps are no-ops on that branch.

The workflow must run in the canonical `springdoc/springdoc-openapi` repository, not a fork:
`release:prepare` pushes to the `<scm>` connection URL, while the preflight and the recovery step
check `origin`; the two agree only when `origin` names the same repository as `<scm>`.

| Input                | Example         | Notes                                                |
| -------------------- | --------------- | ---------------------------------------------------- |
| `releaseVersion`     | `3.1.1`         | Must not exist as a tag or on Maven Central already.  |
| `developmentVersion` | `3.1.2-SNAPSHOT`| Version the release branch keeps afterwards.          |
| `dryRun`             | `false`         | `true` rehearses `release:prepare` and changes nothing on the remote. |

## What a real run does

1. **Test job** — `./mvnw -Pci -T1C test`. Gates the release job, so a release never starts from red code.
2. **Preflight** — refuses a `releaseVersion` whose tag is already on `origin` or whose artifacts are
   already published to Maven Central. Central releases are immutable, so this failure has to happen
   before anything is pushed. It fails closed: if Central cannot be reached the run stops rather than
   assume the version is free.
   The check is not conditional on `dryRun`, so **a rehearsal needs an unused version** — rehearse the
   next patch version, not one that is already out. That is also the version you would release, so a
   rehearsal is worth running with the real candidate.
3. **`release:prepare`** — transforms the POMs to `releaseVersion`, runs its default
   `preparationGoals` (`clean verify`, i.e. the full test suite **on the release POM**, hence the
   second run of the suite in this workflow), commits, tags `v<releaseVersion>`, commits
   `developmentVersion` and **pushes both commits and the tag**.
   `release:perform` runs as a separate step, so it only starts once the tag actually exists.
4. **`release:perform`** — checks out the tag into `target/checkout` and runs `deploy` there with
   `-DskipTests -Dskip.npm=false` (the suite already ran in step 3, and the MCP dashboard must still be
   built from the tagged sources). Deployment goes to the Central Portal through
   `central-publishing-maven-plugin`, which bundles and uploads the modules — including the parent
   POM `org.springdoc:springdoc-openapi` — except `springdoc-openapi-tests`
   (`<skipPublishing>true</skipPublishing>`), signs with the `gpg` profile and waits for validation
   (`autoPublish=true`, `waitUntil=published`).

Once a version is on Central it is immutable: it can never be overwritten or deleted, which is why
the preflight refuses a version that is already there.

There is deliberately no `<distributionManagement>` in `pom.xml`: the Central plugin uses
`publishingServerId` against the Portal API, and a plain `<repository>` URL there would be ignored
while a `<site>` element would silently turn `deploy` into `deploy site-deploy` in step 4.

## Rehearsing

Run with `dryRun: true` and a `releaseVersion` that is not published yet (see the preflight above).
That executes `release:prepare -DdryRun=true -DpushChanges=false`, which writes the bumped POMs next
to the originals and prints the SCM operations instead of committing them, then restores the working
tree and fails the run if anything is left dirty. It is a full trial of step 3 (which is where the
version bugs live). It cannot trial step 4, because `release:perform -DdryRun=true` intentionally
executes no goals at all.

Step 4 is therefore rehearsed separately, by the `Snapshot` workflow: it deploys the same modules
with the same profiles, credentials and plugin through `./mvnw deploy` on every push to a release
line. A broken deploy configuration shows up there, not in a release run.

## Recovering a failed release

`release:prepare` puts the tag and both commits on the remote *before* `release:perform` runs, and
the Maven Release Plugin has no transaction covering the two. When a run fails, its last step
publishes which of the two cases you are in to the run summary.

### Case A — failed before the tag was pushed

Nothing reached `origin`. `release.properties` exists only in the runner, which is discarded, so:

1. Fix the cause (dependency resolution, a flaky test, the GPG key, …).
2. Re-run the workflow with the same versions. `release:prepare` resumes by default (`resume=true`).

### Case B — failed after the tag was pushed

The tag `v<releaseVersion>` and both version-bump commits are on `origin`. `release:perform` may have
uploaded nothing, or uploaded a bundle that is still being validated on the Portal. Nothing is rolled
back automatically, and re-running with the same `releaseVersion` fails the preflight by design.

1. **Check what Central actually received**:
   <https://central.sonatype.com/publishing/deployments>. If the release is there — even only in
   `validated` state — the release is effectively done: publish/drop it from the Portal and do not
   re-run the workflow.
2. Otherwise, note the SHA of the failing run (`v<releaseVersion>`):
   `git fetch origin --tags && git rev-parse 'refs/tags/v<releaseVersion>^{commit}'`
3. Undo the release on the maintenance branch, **only if nobody has pushed on top of it**:
   `git push origin :refs/tags/v<releaseVersion>` and `git push --force-with-lease origin <sha>^:<branch>`.
   If in doubt, do not rewrite the branch — release a new patch version instead.
4. Re-run the workflow with the same versions.
5. If a version is beyond repair, bump the patch number (`3.1.2` instead of `3.1.1`) and delete the
   half-finished tag so it cannot be mistaken for a real release.

## Local equivalents

Useful when diagnosing without touching the CI:

```bash
# What the workflow runs for a real release: prepare first, so that perform only
# starts once the tag exists, then the deploy of that tag.
./mvnw -B -Pci,gpg release:prepare \
  -DreleaseVersion=3.1.1 -DdevelopmentVersion=3.1.2-SNAPSHOT
./mvnw -B -Pci,gpg release:perform -Darguments="-DskipTests -Dskip.npm=false"

# Rehearsal, identical to dryRun: true
./mvnw -B -Pci,gpg release:prepare \
  -DreleaseVersion=3.1.1 -DdevelopmentVersion=3.1.2-SNAPSHOT \
  -DdryRun=true -DpushChanges=false
./mvnw -B -Pci,gpg release:clean   # never pass -DdryRun here: release:clean has no such parameter
```
