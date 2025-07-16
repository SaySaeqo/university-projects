extends "res://List.gd"


# Declare member variables here. Examples:
# var a = 2
# var b = "text"


# Called when the node enters the scene tree for the first time.
func _ready():
	emoticons = [":C",":c",";-;",";;",";n;",";w;",">:3",">:C",">:O",">:c",">:o",\
	">A<",">o<",">u>","C:<","TwT","c:<"]


func _on_Entry_gui_input(event):
	if event is InputEventKey:
		if event.is_action_pressed("ui_down"):
			get_node("../ResetEntries").grab_focus()
		elif event.is_action_pressed("ui_up"):
			$ItemList.grab_focus()
