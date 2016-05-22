package com.antiparagon.cvexperimenter

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

/**
  * Created by wmckay on 5/14/16.
  */
class ImageEditorTab(val img : Image) extends Tab with ExperimenterTab {

  val mousePosLabel = new Label {
    text = "x: 0 y: 0"
    font = Font.font(null, FontWeight.Bold, 18)
    textFill = Color.Green
    padding = Insets(50)
    alignmentInParent = Pos.BaselineLeft
  }

  val currentFrame =  new ImageView(img)
  currentFrame.setOnMouseMoved((e : MouseEvent) => { mousePosLabel.text = s"[ ${e.getX}, ${e.getY}]"; })
  currentFrame.setOnMouseClicked((e : MouseEvent) => { println(s"[${e.getX}, ${e.getY}]"); })

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
              new VBox {
                alignment = Pos.Center
                padding = Insets(20)
                style = BACKGROUND_STYLE
                spacing = 5
                children = Seq(
                  mousePosLabel,
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
