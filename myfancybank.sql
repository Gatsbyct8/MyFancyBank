/*
Navicat MySQL Data Transfer

Source Server         : test1
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : myfancybank

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-11-11 15:34:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `UserID` varchar(255) DEFAULT NULL,
  `accountID` varchar(255) NOT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `currency` varchar(255) NOT NULL,
  `amount` double DEFAULT NULL,
  `transactionNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`accountID`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('yff', '0001', 'saving', 'CNY', '0', '0');
INSERT INTO `account` VALUES ('yff', '0001', 'saving', 'EUR', '0', '0');
INSERT INTO `account` VALUES ('yff', '0001', 'saving', 'USD', '100', '2');
INSERT INTO `account` VALUES ('yff', '0002', 'checking', 'CNY', '0', '0');
INSERT INTO `account` VALUES ('yff', '0002', 'checking', 'EUR', '0', '0');
INSERT INTO `account` VALUES ('yff', '0002', 'checking', 'USD', '100', '2');

-- ----------------------------
-- Table structure for `loan`
-- ----------------------------
DROP TABLE IF EXISTS `loan`;
CREATE TABLE `loan` (
  `UserID` varchar(255) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  `CollateralType` varchar(255) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loan
-- ----------------------------

-- ----------------------------
-- Table structure for `manager`
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `ManagerID` varchar(255) NOT NULL,
  `passcode` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ManagerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manager
-- ----------------------------

-- ----------------------------
-- Table structure for `secureaccount`
-- ----------------------------
DROP TABLE IF EXISTS `secureaccount`;
CREATE TABLE `secureaccount` (
  `UserID` varchar(255) DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  `buyprice` double DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of secureaccount
-- ----------------------------

-- ----------------------------
-- Table structure for `stock`
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `currentprice` double(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock
-- ----------------------------

-- ----------------------------
-- Table structure for `transaction`
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `UserID` varchar(255) DEFAULT NULL,
  `accountID` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `sourceNtargetID` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transaction
-- ----------------------------
INSERT INTO `transaction` VALUES ('yff', '0001', '2019-11-10', '5', 'Deposit', null, '1');
INSERT INTO `transaction` VALUES ('yff', '0001', '2019-11-10', '-5', 'Open account', null, '2');
INSERT INTO `transaction` VALUES ('yff', '0002', '2019-11-10', '5', 'Deposit', null, '3');
INSERT INTO `transaction` VALUES ('yff', '0002', '2019-11-10', '-5', 'Open account', null, '4');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserID` varchar(255) NOT NULL,
  `passcode` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('yff', 'passcode', 'Fang', 'Yifei', '6179389691', '457 Park Drive', 'Boston', 'MA', '02215');
