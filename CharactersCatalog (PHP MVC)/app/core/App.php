<?php

/**
 * Main class for handling MVC routing and controller instantiation
 */
class App
{
    // Defaults
    protected $controller = "home";
    protected $method = "index";
    protected $params = [];

    public function __construct()
    {
        $url = $this->parseUrl();

        // Setting controller
        if (isset($url[0])) {
            if (file_exists("../app/controllers/" . $url[0] . '.php')) {

                $this->controller = $url[0];
                unset($url[0]);
            }
        }
        require_once("../app/controllers/" . $this->controller . ".php");
        $this->controller = new $this->controller;

        // Setting method
        if (isset($url[1])) {
            if (method_exists($this->controller, $url[1])) {
                $this->method = $url[1];
                unset($url[1]);
            }
        }

        // Setting path params
        $this->params = $url ? array_values($url) : [];

        call_user_func_array([$this->controller, $this->method], $this->params);
    }

    public function parseUrl()
    {
        if (isset($_GET['url'])) {
            return explode("/", filter_var(rtrim($_GET["url"], "/"), FILTER_SANITIZE_URL));
        }
    }
}