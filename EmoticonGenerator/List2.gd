extends "res://List.gd"


# Declare member variables here. Examples:
# var a = 2
# var b = "text"


# Called when the node enters the scene tree for the first time.
func _ready():
	emoticons = ["'w'","-o-",".///.",":X",":^|",":o",":x",">.<",">^<",">o<",\
	"XD","Xd","\\o","c:","o-o","o/","o3o","o_o","xD","xd"]


func _on_Entry_gui_input(event):
	if event is InputEventKey:
		if event.is_action_pressed("ui_down"):
			get_node("../ResetEntries").grab_focus()
		elif event.is_action_pressed("ui_up"):
			$ItemList.grab_focus()
