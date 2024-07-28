package dev.newty.fabrishot;

import dev.newty.fabrishot.mixins.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public interface MinecraftInterface {

    MinecraftClient CLIENT = MinecraftClient.getInstance();

    static void resize(int width, int height) {
        WindowAccessor accessor = (WindowAccessor) (Object) CLIENT.getWindow();

        accessor.setWidth(width);
        accessor.setHeight(height);
        accessor.setFramebufferWidth(width);
        accessor.setFramebufferHeight(height);

        CLIENT.onResolutionChanged();
    }

    static int getDisplayWidth() {
        return CLIENT.getWindow().getWidth();
    }

    static int getDisplayHeight() {
        return CLIENT.getWindow().getHeight();
    }

    static void writeFramebuffer(ByteBuffer pb, int bpp) {
        GL11.glReadPixels(0, 0, getDisplayWidth(), getDisplayHeight(), GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pb);

        byte[] line1 = new byte[getDisplayWidth() * bpp];
        byte[] line2 = new byte[getDisplayWidth() * bpp];

        // flip buffer vertically
        for (int i = 0; i < getDisplayHeight() / 2; i++) {
            int ofs1 = i * getDisplayWidth() * bpp;
            int ofs2 = (getDisplayHeight() - i - 1) * getDisplayWidth() * bpp;

            // read lines
            pb.position(ofs1);
            pb.get(line1);
            pb.position(ofs2);
            pb.get(line2);

            // write lines at swapped positions
            pb.position(ofs2);
            pb.put(line1);
            pb.position(ofs1);
            pb.put(line2);
        }
    }
}
