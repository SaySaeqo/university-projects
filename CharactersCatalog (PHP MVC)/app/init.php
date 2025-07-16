<?php
if(!isset($_SESSION)) {
session_start();
}

require_once("dbconfig.php");
require_once("core/App.php");
require_once("core/Controller.php");