package hu.stan.dreamparkour.model.entity;

import hu.stan.dreamparkour.repository.converter.LocalTimeAttributeConverter;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;

@Entity
@Setter
@Getter
@Table(appliesTo = "DbTotalRunTime")
public class DbTotalRunTime {

  @Id
  @Column(name = "run_id", nullable = false, columnDefinition = "BINARY(16)")
  private String runId = UUID.randomUUID().toString();

  @Column(name = "player_id")
  private String playerId;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private DbCourse course;

  @Convert(converter = LocalTimeAttributeConverter.class)
  @Column(name = "run_time", nullable = false, columnDefinition = "TIME(3)")
  private LocalTime runTime;
}
