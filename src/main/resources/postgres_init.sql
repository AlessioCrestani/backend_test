DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS todo;

CREATE TABLE IF NOT EXISTS todo (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(1024) NOT NULL,
    creation_timestamp BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS task (
    id BIGINT NOT NULL PRIMARY KEY,
    todo_id BIGINT NOT NULL,
    description VARCHAR(1024) NOT NULL,
    creation_timestamp BIGINT NOT NULL,
    is_done BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_todo_id
        FOREIGN KEY(todo_id)
        REFERENCES todo(id)
);

INSERT INTO todo VALUES
    (0, 'home', 1665257912000),
    (1, 'work', 1665255912000),
    (3, 'shopping', 1665257992000);

INSERT INTO task VALUES
    (0, 0, 'cleaning', 1665257919000, true),
    (3, 0, 'moving stuff', 1665257919900, false),
    (4, 0, 'organize kitchen', 1665257919901, false),
    (5, 1, 'call boss', 1665257932000, false),
    (6, 1, 'start project X', 1665258918000, false);
