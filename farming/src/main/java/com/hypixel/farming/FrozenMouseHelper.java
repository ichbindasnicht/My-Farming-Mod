package com.hypixel.farming;

import net.minecraft.util.MouseHelper;

public class FrozenMouseHelper extends MouseHelper {
private boolean frozen = false;
    // TODO dont snap when unfreeze
    @Override
    public void mouseXYChange(){
        if (this.frozen){
            this.deltaX = 0;
            this.deltaY = 0;
        } else {
            super.mouseXYChange();
        }
    }

    public void setFrozen(boolean frozen){
        this.frozen = frozen;
        super.mouseXYChange();
        this.deltaX = 0;
        this.deltaY = 0;
    }
}
