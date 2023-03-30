package hu.stan.dreamparkour.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(appliesTo = "DbCheckpoint")
@Where(clause = "deleted = false")
public class DbCheckpoint {

  @Id
  @Column(name = "checkpoint_id", nullable = false, columnDefinition = "BINARY(16)")
  private String checkpointId = UUID.randomUUID().toString();

  @Column(name = "enabled")
  private Boolean enabled;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "start_location_id", referencedColumnName = "location_id")
  private DbLocation startingLocation;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "end_location_id", referencedColumnName = "location_id")
  private DbLocation endingLocation;

  @Column
  private Boolean deleted = false;

  @ManyToOne
  @JoinColumn(name = "course_id", referencedColumnName = "course_id")
  private DbCourse course;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
