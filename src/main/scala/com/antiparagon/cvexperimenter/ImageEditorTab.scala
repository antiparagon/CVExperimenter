package com.antiparagon.cvexperimenter

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox}

/**
  * Created by wmckay on 5/14/16.
  */
class ImageEditorTab(val img : Image) extends Tab with ExperimenterTab {

  val currentFrame =  new ImageView(img)
  currentFrame.setOnMouseClicked((e : MouseEvent) => { println("["+e.getX()+", "+e.getY()+"]"); })
  val editButton = new Button {
    text = "Edit"
    style = BUTTON_STYLE
    onAction = handle {
      println("Pushed button")
    }
  }

  content = new VBox {
    padding = Insets(20)
    style = "-fx-background-color: black"
    alignment = Pos.Center
    children = Seq(
      new HBox {
        alignment = Pos.Center
        children = new ScrollPane {
          style = BACKGROUND_STYLE
          content = new VBox {
            padding = Insets(20)
            style = BACKGROUND_STYLE
            spacing = 5
            children = Seq(
              currentFrame,
              new HBox {
                alignment = Pos.Center
                children = Seq(
                  editButton
                )
              }
            )
          }
        }
      }
    )
  }

  override def getImg() : Image = return img

  override def getTabText(): String = text.value

}
