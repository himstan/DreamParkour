package hu.stan.dreamparkour.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Table;

@Entity
@Getter
@Setter
@Table(appliesTo = "DbCourse")
public class DbCourse {

  @Id
  @Column(name = "course_id", nullable = false, columnDefinition = "BINARY(16)")
  private String courseId = UUID.randomUUID().toString();

  @Column(name = "course_name")
  private String courseName;

  @OrderBy(clause="created_at ASC")
  @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<DbCheckpoint> checkpoints;
}
