CREATE TABLE IF NOT EXISTS users
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email  VARCHAR(256) UNIQUE  NOT NULL,
    name  VARCHAR(512)  NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(256)  NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000)  NOT NULL,
    category_id  BIGINT  NOT NULL,
    confirmed_requests BIGINT,
    created_on TIMESTAMP NOT NULL,
    description  VARCHAR(7000)  NOT NULL,
    event_date TIMESTAMP  NOT NULL,
    initiator_id INTEGER  NOT NULL,
    location_lat  REAL  NOT NULL,
    location_lon  REAL  NOT NULL,
    paid BOOLEAN,
    participant_limit BIGINT NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(256) NOT NULL,
    title VARCHAR(120) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned   BOOLEAN  NOT NULL,
    title  VARCHAR(512)  NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status  VARCHAR(256)  NOT NULL,
    requester  BIGINT  NOT NULL,
    event_id  BIGINT   NOT NULL,
    created  TIMESTAMP  NOT NULL,
    FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    UNIQUE (event_id, requester)

);

CREATE TABLE IF NOT EXISTS events_compilations
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);