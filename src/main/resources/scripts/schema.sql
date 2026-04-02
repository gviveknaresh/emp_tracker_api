

CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL      PRIMARY       KEY,
	employee_code     VARCHAR(100)  NOT NULL UNIQUE,
    name              VARCHAR(255)  NOT NULL,
    department        VARCHAR(100)  NOT NULL,
    role              VARCHAR(100),
    joining_date      DATE          NOT NULL,
	is_active         BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_cycles (
    id          BIGSERIAL   PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
	is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (start_date <= end_date)
);

CREATE TABLE IF NOT EXISTS performance_reviews (
    id               BIGSERIAL    PRIMARY KEY,
    employee_id      BIGINT       NOT NULL,
    cycle_id         BIGINT       NOT NULL,
    rating           INT          CHECK (rating BETWEEN 1 AND 5) NOT NULL,
    reviewer_notes   TEXT,
    submitted_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
	created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	reviewed_by      BIGINT,

    CONSTRAINT fk_employee           FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviewer_employee  FOREIGN KEY (reviewed_by) REFERENCES employees(id) ON DELETE SET NULL,
    CONSTRAINT fk_cycle              FOREIGN KEY (cycle_id) REFERENCES review_cycles(id) ON DELETE CASCADE,

	-- Considering only one record will be created for each employee per cycle --
	CONSTRAINT unique_employee_cycle UNIQUE      (employee_id, cycle_id)
);

CREATE INDEX IF NOT EXISTS idx_reviews_cycle ON performance_reviews(cycle_id);
CREATE INDEX IF NOT EXISTS idx_reviews_employee ON performance_reviews(employee_id);

CREATE TABLE IF NOT EXISTS goals (
    id           BIGSERIAL    PRIMARY KEY,
    employee_id  BIGINT       NOT NULL,
    cycle_id     BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    status       VARCHAR(20)  CHECK (status IN ('PENDING', 'COMPLETED', 'MISSED')),
	created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_goal_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_goal_cycle FOREIGN KEY (cycle_id) REFERENCES review_cycles(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_goals_cycle ON goals(cycle_id);
CREATE INDEX IF NOT EXISTS idx_employees_department ON employees(department);