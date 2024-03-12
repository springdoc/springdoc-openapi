package test.org.springdoc.api.v31.app8;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class ExamplesResponse {
	
	@Schema(description = "self's user info", requiredMode = RequiredMode.REQUIRED)
	private UserInfo self;

	@Schema(description = "friend, deprecated, use friends instead", requiredMode = RequiredMode.NOT_REQUIRED)
	@Deprecated
	private UserInfo friend;

	@Schema(description = "friends", requiredMode = RequiredMode.NOT_REQUIRED)
	private List<UserInfo> friends;

	public ExamplesResponse(UserInfo self, UserInfo friend, List<UserInfo> friends) {
		this.self = self;
		this.friend = friend;
		this.friends = friends;
	}

	public UserInfo getSelf() {
		return self;
	}

	public void setSelf(UserInfo self) {
		this.self = self;
	}

	public UserInfo getFriend() {
		return friend;
	}

	public void setFriend(UserInfo friend) {
		this.friend = friend;
	}

	public List<UserInfo> getFriends() {
		return friends;
	}

	public void setFriends(List<UserInfo> friends) {
		this.friends = friends;
	}
}
