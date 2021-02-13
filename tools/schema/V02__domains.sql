CREATE TABLE IF NOT EXISTS `domain` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `zone` VARCHAR(128) NULL,
  `region` VARCHAR(128) NULL,
  `key_value` VARCHAR(128) NULL,
  `key_id` VARCHAR(128) NULL,
  `enabled` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `domain_name` (`name` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `hostname` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `domain_id` INT NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `timestamp` BIGINT(64) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `domain_hostname`
    FOREIGN KEY (`domain_id`)
    REFERENCES `domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

alter table stat add column certs bigint(64);

