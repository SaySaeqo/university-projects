<?php
class Home extends Controller
{
    public function index()
    {
        // Loads models
        $character = $this->model("Character");
        
        // Get filters on view and select
        $name = isset($_GET["name"]) ? $_GET["name"] : "";
        $location = isset($_GET["location"]) ? $_GET["location"] : "all";

        // Handle creating a new character
        if ($_SERVER["REQUEST_METHOD"] == "POST"){
            if(isset($_FILES["image"])){
                $name = $character->newEntry($_POST, $_FILES["image"]);
            } else{
                $name = $character->newEntry($_POST);
            }
            header("Location: ".$_SERVER['PHP_SELF']."?"."name=".$name."&location=".$location);
            exit;
        }

        // Render the view
        $this->view("home/index", [
            "view" => $character->byName($name),
            "select" => $character->byLocation($location),
            "locations" => $character->allLocations(),
            "curr_location" => $location
        ]);
    }
}
