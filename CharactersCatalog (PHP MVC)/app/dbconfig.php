<?php

/**
 * Local database's interface
 */
class Database
{
    protected $data_dir = __DIR__."/mydb/";
    protected $images_dir = __DIR__."/images/";
    protected $dbentities = [];

    public function __construct()
    {
        foreach (scandir($this->data_dir) as $file) {
            if ($file and $file != "." and $file != "..") {
                $json = file_get_contents($this->data_dir . $file);
                $data = json_decode($json, true);
                if ($data["image"] == "") {
                    $data["image"] = "images/no_image.png";
                }
                array_push($this->dbentities, $data);
            }
        }
    }

    function add($entity, $image = ["name" => ""])
    {
        // Setting for image
        if ($image["name"] != "") {
            $path = $this->images_dir . $entity["name"] . "." . pathinfo($image["name"], PATHINFO_EXTENSION);
            move_uploaded_file($image['tmp_name'], $path);
            $entity["image"] = "/app/images/" . $entity["name"] . "." . pathinfo($image["name"], PATHINFO_EXTENSION);
        } else if ($entity["image"] == "") {
            $entity["image"] = "/app/images/no_image.png";
        }

        // Setting for color
        if ($entity["color"] == "#c0c0c0") {
            unset($entity["color"]);
        }

        // Saving as json
        $file_path = $this->data_dir . $entity["name"] . ".json";
        $json = json_encode($entity, JSON_PRETTY_PRINT);

        if(file_put_contents($file_path, $json)){
            return $entity["name"];
        }
        return false;
    }

    function query($query = []){
        $postfiltered = $this->dbentities;

        foreach ($query as $key => $value){
            $prefiltered = $postfiltered;
            $postfiltered = [];

            // Filter
            foreach ($prefiltered as $entity) {
                if (isset($entity[$key]) and $entity[$key] == $value){
                    array_push($postfiltered, $entity);
                }
            }
        }
        return $postfiltered;
    }

    function allValues($key){
        $values = [];
        foreach ($this->dbentities as $entity) {
            if (isset($entity[$key]) and !in_array($entity[$key], $values)){
                array_push($values, $entity[$key]);
            }
        }
        return $values;
    }
}

