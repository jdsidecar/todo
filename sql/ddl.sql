create database todo;
use todo;

create table todo (
	id INT AUTO_INCREMENT PRIMARY KEY,
   	text  VARCHAR(256) UNIQUE,
	completed BOOLEAN DEFAULT FALSE, 
   	created_at TIMESTAMP NOT NULL,
   	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

