<?php

/**
 * Base controller class
 */
class Controller{

    /**
     * Loads a model
     */
    public function model($model){
        require_once "../app/models/" . $model . ".php";
        return new $model();
    }


    /**
     * Loads a view
     */
    public function view($view, $data=[]){
        require_once ("../app/views/" . $view . ".php");
    }

}