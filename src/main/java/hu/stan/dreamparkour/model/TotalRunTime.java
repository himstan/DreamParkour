package hu.stan.dreamparkour.model;

import java.time.LocalTime;
import java.util.UUID;

public record TotalRunTime(
    UUID runId,
    UUID courseId,
    UUID playerId,
    LocalTime runTime
) {}
