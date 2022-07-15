<?php
class Home extends Controller
{
    public function index()
    {
        $name = "";
        $location = "all";
        if (isset($_GET["name"])){
            $name = $_GET["name"];
        }
        if (isset($_GET["location"])){
            $location = $_GET["location"];
        }


        $character = $this->model("Character");

        if ($_SERVER["REQUEST_METHOD"] == "POST"){
            if(isset($_FILES["image"])){
                $name = $character->newEntry($_POST, $_FILES["image"]);
            } else{
                $name = $character->newEntry($_POST);
            }
            header("Location: ".$_SERVER['PHP_SELF']."?"."name=".$name."&location=".$location);
            exit;
        }
        
        $data = [
            "view" => $character->byName($name),
            "select" => $character->byLocation($location),
            "locations" => $character->allLocations(),
            "curr_location" => $location
        ];

        $this->view("home/index", $data);
    }
}
