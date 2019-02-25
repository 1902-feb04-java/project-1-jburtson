CREATE SCHEMA proj1;
SET search_path TO proj1, public;

CREATE TABLE managers
(
	id SERIAL NOT NULL,
	username VARCHAR(160) NOT NULL UNIQUE,
	password VARCHAR(160) NOT NULL,
	first_name VARCHAR(160) NOT NULL,
	last_name VARCHAR(160) NOT NULL,
	CONSTRAINT pk_managers PRIMARY KEY  (id),
	CONSTRAINT managers_password_constraint CHECK (LENGTH(password)>=8)
);

CREATE TABLE employees
(
	id SERIAL NOT NULL,
	username VARCHAR(160) NOT NULL UNIQUE,
	password VARCHAR(160) NOT NULL,
	first_name VARCHAR(160) NOT NULL,
	last_name VARCHAR(160) NOT NULL,
	CONSTRAINT pk_employees PRIMARY KEY  (id),
	CONSTRAINT employees_password_constraint CHECK (LENGTH(password)>=8)
);

CREATE TABLE reimbursement_requests
(
	id SERIAL NOT NULL,
	amount int NOT NULL,
	employee_id int NOT NULL,
	isResolved BOOLEAN DEFAULT false NOT NULL,
	isPending BOOLEAN DEFAULT true NOT NULL,
	isApproved BOOLEAN NULL,
	resolvedBy int NULL,
	recipt_img bytea NULL,
	CONSTRAINT pk_reimbursement_requests PRIMARY KEY  (id),
	CONSTRAINT fk_manager_id FOREIGN KEY  (resolvedBy) REFERENCES managers(id),
	CONSTRAINT fk_employee_id FOREIGN KEY  (employee_id) REFERENCES employees(id)
);