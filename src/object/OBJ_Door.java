package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends SuperObject{
    GamePanel gp;

    public OBJ_Door(GamePanel gp) {
        super(gp);
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "door";
        objectType = props;
        this.gp = gp;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/doors.png"));
            image = uTool.scaleImage(image,gp.tileSize,gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
        collision = true;
        setDialogues();
    }
    public void setDialogues(){
        dialogues[0]= "Doors lock !";
        dialogues[1]= "Doors open !";
    }
    public void speak(int index){
        gp.ui.currentDialogue =dialogues[index];


    }


}
