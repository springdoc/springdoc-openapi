package test.org.springdoc.api.v31.app8;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record ExamplesResponse(
        @Schema(description = "self's user info", requiredMode = REQUIRED)
        UserInfo self,
        @Schema(description = "friend, deprecated, use friends instead", requiredMode = NOT_REQUIRED)
        @Deprecated
        UserInfo friend,
        @Schema(description = "friends", requiredMode = NOT_REQUIRED)
        List<UserInfo> friends
) {
}
