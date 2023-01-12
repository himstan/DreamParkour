package hu.stan.dreamparkour.model.entity;

import hu.stan.dreamparkour.repository.converter.LocalTimeAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(appliesTo = "DbSplitRunTime")
public class DbSplitRunTime {

  @Id
  @Column(name = "split_run_id", nullable = false, columnDefinition = "BINARY(16)")
  private String splitRunId = UUID.randomUUID().toString();

  @Column(name = "player_id")
  private String playerId;

  @ManyToOne
  private DbCheckpoint checkpoint;

  @ManyToOne
  private DbTotalRunTime totalRun;

  @Convert(converter = LocalTimeAttributeConverter.class)
  @Column(name = "split_time")
  private LocalTime splitTime;
}
