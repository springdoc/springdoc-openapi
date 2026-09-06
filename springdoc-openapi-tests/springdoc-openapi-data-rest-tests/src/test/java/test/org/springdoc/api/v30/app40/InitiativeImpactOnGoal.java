/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.v30.app40;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 * Join entity combining an embedded identifier with two mapped associations.
 *
 * @author hej090224
 */
@Entity
public class InitiativeImpactOnGoal {

	@EmbeddedId
	private GoalInitiativeImpactId id;

	@ManyToOne
	@MapsId("goalId")
	@JoinColumn(name = "goal_id")
	private Goal goal;

	@ManyToOne
	@MapsId("initiativeId")
	@JoinColumn(name = "initiative_id")
	private Initiative initiative;

	@Enumerated(EnumType.STRING)
	private ImpactLevel impactLevel;

	public GoalInitiativeImpactId getId() {
		return id;
	}

	public Goal getGoal() {
		return goal;
	}

	public Initiative getInitiative() {
		return initiative;
	}

	public ImpactLevel getImpactLevel() {
		return impactLevel;
	}

	enum ImpactLevel {
		TRIVIAL,
		SIGNIFICANT
	}
}
