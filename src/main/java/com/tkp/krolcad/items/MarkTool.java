package com.tkp.krolcad.items;

import com.tkp.krolcad.KrolCAD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MarkTool extends Item {
    private static final String pointA = "PointA";
    private static final String pointB = "PointB";

    // Хранилище координат выделения для каждого игрока по UUID
    public static final Map<UUID, Map<String, int[]>> toolCoords = new HashMap<>();

    public MarkTool() {
        this.setUnlocalizedName("markTool");
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setTextureName("krolcad:mark_tool");
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        return handleClick(x, y, z, player, pointA);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                             int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return handleClick(x, y, z, player, pointB);
    }

    private boolean handleClick(int x, int y, int z, EntityPlayer player, String point) {
        if (!player.worldObj.isRemote) {
            UUID uuid = player.getUniqueID();
            toolCoords.computeIfAbsent(uuid, k -> new HashMap<>())
                .put(point, new int[]{x, y, z});

            String msg = String.format("%s set: [%d, %d, %d]", point, x, y, z);
            KrolCAD.LOG.info(msg);
            player.addChatMessage(new ChatComponentText(msg));
            return true;
        }
        return false;
    }

    public static int[] getMinXYZ(UUID uuid) {
        int[] a = toolCoords.get(uuid).get(pointA);
        int[] b = toolCoords.get(uuid).get(pointB);
        return new int[]{
            Math.min(a[0], b[0]),
            Math.min(a[1], b[1]),
            Math.min(a[2], b[2])
        };
    }

    public static int[] getMaxXYZ(UUID uuid) {
        int[] a = toolCoords.get(uuid).get(pointA);
        int[] b = toolCoords.get(uuid).get(pointB);
        return new int[]{
            Math.max(a[0], b[0]),
            Math.max(a[1], b[1]),
            Math.max(a[2], b[2])
        };
    }

    public static void renderSelectionGrid(UUID uuid, float partialTicks) {
        if (!toolCoords.containsKey(uuid)) return;
        Map<String, int[]> points = toolCoords.get(uuid);
        if (!points.containsKey(pointA) || !points.containsKey(pointB)) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        int[] min = getMinXYZ(uuid);
        int[] max = getMaxXYZ(uuid);

        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5F);

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
            min[0], min[1], min[2],
            max[0] + 1, max[1] + 1, max[2] + 1
        ).offset(-px, -py, -pz);

        Tessellator tess = Tessellator.instance;

        // Заливка граней
        GL11.glColor4f(0f, 1f, 1f, 0.15f);
        GL11.glDepthMask(false);
        tess.startDrawingQuads();
        drawFilledBox(tess, box);
        tess.draw();
        GL11.glDepthMask(true);

        // Обводка
        GL11.glColor4f(0f, 1f, 1f, 0.4f);
        tess.startDrawing(GL11.GL_LINES);
        drawOutlinedBox(tess, box);
        tess.draw();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    private static void drawOutlinedBox(Tessellator t, AxisAlignedBB box) {
        double x1 = box.minX, y1 = box.minY, z1 = box.minZ;
        double x2 = box.maxX, y2 = box.maxY, z2 = box.maxZ;

        // нижняя и верхняя грани + вертикали
        t.addVertex(x1, y1, z1); t.addVertex(x2, y1, z1);
        t.addVertex(x2, y1, z1); t.addVertex(x2, y1, z2);
        t.addVertex(x2, y1, z2); t.addVertex(x1, y1, z2);
        t.addVertex(x1, y1, z2); t.addVertex(x1, y1, z1);

        t.addVertex(x1, y2, z1); t.addVertex(x2, y2, z1);
        t.addVertex(x2, y2, z1); t.addVertex(x2, y2, z2);
        t.addVertex(x2, y2, z2); t.addVertex(x1, y2, z2);
        t.addVertex(x1, y2, z2); t.addVertex(x1, y2, z1);

        t.addVertex(x1, y1, z1); t.addVertex(x1, y2, z1);
        t.addVertex(x2, y1, z1); t.addVertex(x2, y2, z1);
        t.addVertex(x2, y1, z2); t.addVertex(x2, y2, z2);
        t.addVertex(x1, y1, z2); t.addVertex(x1, y2, z2);
    }

    private static void drawFilledBox(Tessellator t, AxisAlignedBB box) {
        double x1 = box.minX, y1 = box.minY, z1 = box.minZ;
        double x2 = box.maxX, y2 = box.maxY, z2 = box.maxZ;

        t.addVertex(x1, y2, z1); t.addVertex(x2, y2, z1);
        t.addVertex(x2, y2, z2); t.addVertex(x1, y2, z2);

        t.addVertex(x1, y1, z1); t.addVertex(x2, y1, z1);
        t.addVertex(x2, y1, z2); t.addVertex(x1, y1, z2);

        t.addVertex(x1, y1, z1); t.addVertex(x2, y1, z1);
        t.addVertex(x2, y2, z1); t.addVertex(x1, y2, z1);

        t.addVertex(x1, y1, z2); t.addVertex(x2, y1, z2);
        t.addVertex(x2, y2, z2); t.addVertex(x1, y2, z2);

        t.addVertex(x1, y1, z1); t.addVertex(x1, y1, z2);
        t.addVertex(x1, y2, z2); t.addVertex(x1, y2, z1);

        t.addVertex(x2, y1, z1); t.addVertex(x2, y1, z2);
        t.addVertex(x2, y2, z2); t.addVertex(x2, y2, z1);
    }

    // вручню сохраняем коорлинату, даже если евент поломки блока отменится
    public static void handleClickFromEvent(EntityPlayer player, int x, int y, int z) {
        UUID uuid = player.getUniqueID();
        if (!toolCoords.containsKey(uuid)) {
            toolCoords.put(uuid, new HashMap<>());
        }
        toolCoords.get(uuid).put("PointA", new int[]{x, y, z});

        String msg = String.format("PointA set: [%d, %d, %d]", x, y, z);
        player.addChatMessage(new ChatComponentText(msg));
    }
}
