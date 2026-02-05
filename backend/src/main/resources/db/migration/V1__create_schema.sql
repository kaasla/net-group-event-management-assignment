CREATE TABLE events (
  id               BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name             VARCHAR(255) NOT NULL,
  date_time        TIMESTAMPTZ  NOT NULL,
  max_participants INTEGER      NOT NULL CHECK (max_participants > 0),
  created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_events_date_time ON events (date_time);

CREATE TABLE participants (
  id            BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  event_id      BIGINT       NOT NULL REFERENCES events (id) ON DELETE CASCADE,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  personal_code CHAR(11)     NOT NULL,
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),

  CONSTRAINT uq_participant_event UNIQUE (event_id, personal_code)
);

CREATE INDEX idx_participants_event_id ON participants (event_id);