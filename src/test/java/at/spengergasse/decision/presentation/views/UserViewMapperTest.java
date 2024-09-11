package at.spengergasse.decision.presentation.views;

import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.fixture.UserFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserViewMapperTest {

    UserViewMapper mapper = UserViewMapper.INSTANCE;

    @Test
    public void toUserView_shouldMapUserToUserView() {

        // Given
        User user = UserFixture.createUser();

        // When
        Views.UserView userView = mapper.toUserView(user);

        // Then
        assertThat(userView, notNullValue());
        assertThat(userView.id(), equalTo(user.getId()));
    }
}
