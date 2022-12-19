package hu.stan.dreamparkour.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;

@Entity
@Setter
@Getter
@Table(appliesTo = "DbCheckpoint")
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

  @ManyToOne
  private DbCourse course;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
