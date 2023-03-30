-- Create DbCourse table
CREATE TABLE DbCourse (
                          course_id BINARY(16) NOT NULL,
                          course_name VARCHAR(255),
                          PRIMARY KEY (course_id)
);

-- Create DbLocation table
CREATE TABLE DbLocation (
                            location_id BINARY(16) NOT NULL,
                            world_name VARCHAR(255) NOT NULL,
                            x DOUBLE PRECISION,
                            y DOUBLE PRECISION,
                            z DOUBLE PRECISION,
                            PRIMARY KEY (location_id)
);

-- Create DbCheckpoint table
CREATE TABLE DbCheckpoint (
                              checkpoint_id BINARY(16) NOT NULL,
                              enabled BOOLEAN,
                              start_location_id BINARY(16),
                              end_location_id BINARY(16),
                              deleted BOOLEAN NOT NULL DEFAULT FALSE,
                              course_id BINARY(16),
                              created_at TIMESTAMP,
                              PRIMARY KEY (checkpoint_id),
                              FOREIGN KEY (start_location_id) REFERENCES DbLocation(location_id),
                              FOREIGN KEY (end_location_id) REFERENCES DbLocation(location_id),
                              FOREIGN KEY (course_id) REFERENCES DbCourse(course_id)
);

-- Create DbTotalRunTime table
CREATE TABLE DbTotalRunTime (
                                run_id BINARY(16) NOT NULL,
                                player_id VARCHAR(255),
                                course_id BINARY(16),
                                run_time TIME,
                                PRIMARY KEY (run_id),
                                FOREIGN KEY (course_id) REFERENCES DbCourse(course_id)
);

-- Create DbSplitRunTime table
CREATE TABLE DbSplitRunTime (
                                split_run_id BINARY(16) NOT NULL,
                                player_id VARCHAR(255),
                                checkpoint_id BINARY(16),
                                run_id BINARY(16),
                                split_time TIME,
                                PRIMARY KEY (split_run_id),
                                FOREIGN KEY (checkpoint_id) REFERENCES DbCheckpoint(checkpoint_id),
                                FOREIGN KEY (run_id) REFERENCES DbTotalRunTime(run_id)
);
