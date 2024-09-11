package at.spengergasse.decision.persistence;

import at.spengergasse.decision.config.MongoConfig;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import(MongoConfig.class)
public class UserRepositoryTest {
  @Autowired private UserRepository userRepository;

  private User userSaved;

  @BeforeEach
  public void setup() {
    var user = UserFixture.createUser();

    userRepository.deleteAll();
    userSaved = userRepository.save(user);
  }

  @Test
  public void saveUser_shouldReturnSavedUser() {
    assertThat(userSaved, notNullValue());
  }

  @Test
  public void saveUser_shouldSetAuditFields() {
    // Then
    assertThat(userSaved.getCreatedAt(), notNullValue());
    assertThat(userSaved.getLastModifiedAt(), notNullValue());
    assertThat(userSaved.getVersion(), notNullValue());
    assertThat(userSaved.getVersion(), equalTo(0L));
  }

  @Test
  public void findById_shouldReturnUser_whenUserExists() {
    // When
    var userFound = userRepository.findById(userSaved.getId());

    // Then
    assertThat(userFound.isPresent(), is(true));
    assertThat(userFound.get().getId(), equalTo(userSaved.getId()));
  }

  @Test
  public void findByEmail_shouldReturnUser_whenUserExists() {
    // When
    Optional<User> userFound = userRepository.findByEmail(userSaved.getEmail());

    // Then
    assertThat(userFound.isPresent(), is(true));
    assertThat(userFound.get().getEmail(), equalTo(userSaved.getEmail()));
  }

  @Test
  public void saveUser_shouldFail_withDuplicateEmail() {
    // Given
    var duplicatedUser = UserFixture.createUser();

    // When / Then
    assertThrows(DuplicateKeyException.class,
            () -> userRepository.save(duplicatedUser));
  }

  @Test
  public void saveUser_shouldFail_withOldVersion() {
    // When
    // version = 0
    var userRead1 = userRepository.findById(userSaved.getId()).get();
    var userRead2 = userRepository.findById(userSaved.getId()).get();

    // When
    // userRead1 version = 0; DB version = 0 -> version 1
    userRepository.save(userRead1);

    // userRead2 version = 0; DB version = 1
    // Has it been modified meanwhile
    assertThrows(OptimisticLockingFailureException.class,
            () -> userRepository.save(userRead2));
  }
}
