package net.sixik.sdmmarket.common.ftb;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ImageConfig;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.net.SetCustomImageMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ConfigIconItemStack extends ItemStackConfig {

    public ConfigIconItemStack() {
        super(false, true);
    }

    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            if (button.isRight()) {
                //ImageConfig imageConfig = new ImageConfig();
                //(new SelectImageResourceScreen(imageConfig, (accepted) -> {
                //    if (accepted) {
                //        if (!((ResourceLocation)imageConfig.getValue()).equals(ImageResourceConfig.NONE)) {
                //            ItemStack stack = new ItemStack((ItemLike) ItemsRegister.CUSTOM_ICON.get());
                //            stack.addTagElement("Icon", StringTag.valueOf(((ResourceLocation)imageConfig.getValue()).toString()));
                //            this.setCurrentValue(stack);
                //        } else {
                //            this.setCurrentValue(ItemStack.EMPTY);
                //        }
                //    }
//
                //    callback.save(accepted);
                //})).openGui();


            } else {
                (new SelectItemStackScreen(this, callback)).openGui();
            }
        }

    }

    public void openCustomIconGui(Player player, InteractionHand hand) {
        ImageConfig config = new ImageConfig();
        config.onClicked(MouseButton.LEFT, b -> {
            if (b) {
                if (config.value.isEmpty()) {
                    player.getItemInHand(hand).removeTagKey("Icon");
                } else {
                    player.getItemInHand(hand).addTagElement("Icon", StringTag.valueOf(config.value));
                }

                new SetCustomImageMessage(hand, config.value).sendToServer();
            }

            Minecraft.getInstance().setScreen(null);
        });
    }
}
