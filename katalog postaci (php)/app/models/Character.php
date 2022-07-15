<?php

class Character
{
    protected $mydb;

    public function __construct()
    {
        $this->mydb = new Database();
    }

    function byName($name = "")
    {
        $query = [
            "name" => $name
        ];
        $result = $this->mydb->query($query);
        // if not empty get first result
        if ($result){
            $result = $result[0];
        }
        return $result;
    }

    function byLocation($location = "all"){
        if ($location == "all"){
            return $this->mydb->query();
        }

        $query = [
            "location" => $location
        ];
        return $this->mydb->query($query);
    }

    function newEntry($entry, $image){
        return $this->mydb->add($entry, $image);
    }

    function allLocations(){
        $locations = ["all"];
        return array_merge($locations, $this->mydb->allValues("location"));;
    }
}
