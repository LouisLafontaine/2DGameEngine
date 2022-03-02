package zion;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{
    
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;
    
    public LevelEditorScene() {
        System.out.println("Inside Level editor scene");
    }
    
    @Override
    public void update(float dt) {
        System.out.println("" + (1f / dt) + " fps");
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
            
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
