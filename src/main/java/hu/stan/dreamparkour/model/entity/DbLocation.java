package hu.stan.dreamparkour.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;

@Entity
@Setter
@Getter
@Table(appliesTo = "DbLocation")
public class DbLocation {

  @Id
  @Column(name = "location_id", nullable = false, columnDefinition = "BINARY(16)")
  private String locationId = UUID.randomUUID().toString();

  @Column(name = "world_name", nullable = false)
  private String worldName;

  @Column(name = "x")
  private double x;

  @Column(name = "y")
  private double y;

  @Column(name = "z")
  private double z;
}
