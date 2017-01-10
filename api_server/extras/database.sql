DROP DATABASE IF EXISTS sg;
CREATE DATABASE IF NOT EXISTS sg;
USE sg;

DROP TABLE IF EXISTS servers;
CREATE TABLE IF NOT EXISTS servers(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) DEFAULT NULL,
  email TEXT DEFAULT NULL,
  sim_serial TEXT NOT NULL,
  imei VARCHAR (20) NOT NULL,
  device_name VARCHAR (50) NOT NULL,
  device_hash TEXT NOT NULL,
  server_key VARCHAR (10) NOT NULL,
  fcm_id TEXT NOT NULL,
  country_id INT(4) NOT NULL DEFAULT 91,
  is_active TINYINT(4) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id),
  UNIQUE KEY (imei)
  );




CREATE TABLE users(
   id INT(11) NOT NULL AUTO_INCREMENT,
   email VARCHAR(100) NOT NULL,
   api_key VARCHAR (10) NOT NULL,
   is_active TINYINT(4)  NOT NULL  DEFAULT 1 ,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

   PRIMARY KEY (id)
   );


CREATE TABLE sms_requests(
  id INT NOT NULL AUTO_INCREMENT,
  message TEXT CHARACTER SET UTF8 NOT NULL,
  server_id INT NOT NULL,
  user_id INT NOT NULL,
  is_active TINYINT(4) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY(id),
  FOREIGN KEY (server_id) REFERENCES servers(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
  );

CREATE TABLE recipients(
  id INT NOT NULL AUTO_INCREMENT,
  sms_request_id INT NOT NULL,
  recipient VARCHAR (15) NOT NULL,
  is_active TINYINT(4) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id),
  FOREIGN KEY (sms_request_id) REFERENCES sms_requests(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sms_request_statuses(
  id INT NOT NULL AUTO_INCREMENT,
  recipient_id INT NOT NULL,
  status ENUM('SENT_TO_SERVER','DELIVERED_TO_SERVER','FAILED_TO_SEND_TO_SERVER','SENT_TO_RECIPIENT','DELIVERED_TO_RECIPIENT','FAILED_TO_SEND_TO_RECIPIENT') NOT NULL DEFAULT 'SENT_TO_SERVER',
  occurred_at BIGINT NOT NULL ,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active TINYINT(4) NOT NULL DEFAULT 1,
  PRIMARY KEY(id),
  FOREIGN KEY (recipient_id) REFERENCES recipients(id) ON DELETE CASCADE ON UPDATE CASCADE
  );

DELIMITER $$

CREATE TRIGGER after_recipient_insert
AFTER INSERT ON recipients
FOR EACH ROW BEGIN
    INSERT INTO sms_request_statuses
    SET recipient_id = NEW.id,
      occurred_at = UNIX_TIMESTAMP(NOW());
END$$

DELIMITER ;


CREATE TABLE IF NOT EXISTS preference (
  id int(11) NOT NULL AUTO_INCREMENT,
  _key varchar(100) NOT NULL,
  _value text NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY _key (_key)
);

INSERT INTO preference (_key, _value) VALUES
('email_username', 'mymailer64@gmail.com'),
('email_password', 'mypassword64'),
('admin_email', 'theapache64@gmail.com');

SELECT
  r.recipient,
  sqs.status,
  sqs.occurred_at
FROM sms_request_statuses sqs
INNER JOIN recipients r ON r.id = sqs.recipient_id
INNER JOIN sms_requests sr ON r.sms_request_id = sr.id WHERE sr.id = 49 AND sr.user_id = 1
GROUP BY sqs.id;