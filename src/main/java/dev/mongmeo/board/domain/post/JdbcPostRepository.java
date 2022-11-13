package dev.mongmeo.board.domain.post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class JdbcPostRepository implements PostRepository {

  @Value("${database.url}")
  private String url;
  @Value("${database.username}")
  private String username;
  @Value("${database.password}")
  private String password;
  private Connection connection;

  @PostConstruct
  public void init() {
    try {
      connection = DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      log.error("cannot connect to database url : {}", url, e);
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  public void tearDown() throws SQLException {
    connection.close();
  }

  @Override
  public boolean existPostById(Long postId) {
    String sql = "SELECT * FROM post WHERE id=?";
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.prepareStatement(sql);
      statement.setLong(1, postId);
      resultSet = statement.executeQuery();

      return resultSet.next();
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public Optional<PostEntity> findPostById(Long postId) {
    String sql = "SELECT * FROM post WHERE id=?";
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.prepareStatement(sql);
      statement.setLong(1, postId);
      resultSet = statement.executeQuery();

      return Optional.ofNullable(getPostEntityMapFromResultSet(resultSet).get(postId));
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public List<PostEntity> findPosts() {
    String sql = "SELECT * FROM post";
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sql);

      return new ArrayList<>(getPostEntityMapFromResultSet(resultSet).values());
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public List<PostEntity> findPosts(int page, int size) {
    String sql = "SELECT * FROM post LIMIT ? OFFSET ?";
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.prepareStatement(sql);
      statement.setInt(1, size);
      statement.setInt(2, size * (page - 1));
      resultSet = statement.executeQuery();

      return new ArrayList<>(getPostEntityMapFromResultSet(resultSet).values());
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public PostEntity save(PostEntity entity) {
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = "";
    try {
      if (entity.getId() == null) {
        sql = "INSERT INTO post(title, content, author) VALUES (?, ?, ?)";
        statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getTitle());
        statement.setString(2, entity.getContent());
        statement.setString(3, entity.getAuthor());
      } else {
        sql = "UPDATE post SET title=?, content=?, author=? WHERE id=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, entity.getTitle());
        statement.setString(2, entity.getContent());
        statement.setString(3, entity.getAuthor());
        statement.setLong(4, entity.getId());
      }
      long key;

      statement.executeUpdate();

      if (entity.getId() == null) {
        resultSet = statement.getGeneratedKeys();
        resultSet.next();
        key = resultSet.getLong(1);
      } else {
        key = entity.getId();
      }
      closeResource(statement, resultSet);

      statement = connection.prepareStatement("SELECT * FROM post WHERE id=?");
      statement.setLong(1, key);
      resultSet = statement.executeQuery();

      return getPostEntityMapFromResultSet(resultSet).get(key);

    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public void deletePostsById(Long postId) {
    String sql = "DELETE FROM post WHERE id=?";
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.prepareStatement(sql);
      statement.setLong(1, postId);
      statement.executeUpdate();
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  @Override
  public long count() {
    String sql = "SELECT COUNT(*) FROM post";
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sql);
      resultSet.next();
      return resultSet.getLong(1);
    } catch (SQLException e) {
      log.error("Exception occurred while executing query ({})", sql);
      throw new RuntimeException(e);
    } finally {
      closeResource(statement, resultSet);
    }
  }

  private Map<Long, PostEntity> getPostEntityMapFromResultSet(ResultSet resultSet)
      throws SQLException {
    Map<Long, PostEntity> posts = new HashMap<>();
    while (resultSet.next()) {
      PostEntity post = PostEntity.builder()
          .id(resultSet.getLong("id"))
          .title(resultSet.getString("title"))
          .content(resultSet.getString("content"))
          .author(resultSet.getString("author"))
          .build();
      String createdAt = resultSet.getString("created_at");
      post.setCreatedAt(LocalDateTime.parse(createdAt.substring(0, createdAt.lastIndexOf('.')),
          DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")));
      posts.put(post.getId(), post);
    }
    return posts;
  }

  private void closeResource(Statement statement, ResultSet resultSet) {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
    } catch (SQLException e) {
      log.error("Exception occurred while closing resource");
      throw new RuntimeException(e);
    }
  }
}
