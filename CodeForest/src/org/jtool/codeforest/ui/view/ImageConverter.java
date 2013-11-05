/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * Converts an SWT-based image into an AWT-based image.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama 
 */
public class ImageConverter {
    
    private static ImageConverter imageLoader = new ImageConverter();
    
    private ImageConverter() {
    }
    
    public static ImageConverter getInstance() {
        return imageLoader;
    }
    
    public BufferedImage convertToAWT(ImageData data) {
        ColorModel colorModel = null;
        PaletteData palette = data.palette;
        
        if (palette.isDirect) {
            colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
            WritableRaster raster = colorModel.createCompatibleWritableRaster(data.width, data.height);
            BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, null);
            WritableRaster braster = bufferedImage.getRaster();
            
            int[] pixelArray = new int[3];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int pixel = data.getPixel(x, y);
                    RGB rgb = palette.getRGB(pixel);
                    pixelArray[0] = rgb.red;
                    pixelArray[1] = rgb.green;
                    pixelArray[2] = rgb.blue;
                    braster.setPixels(x, y, 1, 1, pixelArray);
                }
            }
            return bufferedImage;
            
        } else {
            RGB[] rgbs = palette.getRGBs();
            byte[] red = new byte[rgbs.length];
            byte[] green = new byte[rgbs.length];
            byte[] blue = new byte[rgbs.length];
            for (int i = 0; i < rgbs.length; i++) {
                RGB rgb = rgbs[i];
                red[i] = (byte) rgb.red;
                green[i] = (byte) rgb.green;
                blue[i] = (byte) rgb.blue;
            }
            if (data.transparentPixel != -1) {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
            } else {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
            }
            WritableRaster raster = colorModel.createCompatibleWritableRaster(data.width, data.height);
            BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, null);
            WritableRaster braster = bufferedImage.getRaster();
            
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int pixel = data.getPixel(x, y);
                    pixelArray[0] = pixel;
                    braster.setPixel(x, y, pixelArray);
                }
            }
            return bufferedImage;
        }
    }
    
    public ImageData convertToSWT(BufferedImage bufferedImage) {
        if (bufferedImage.getColorModel() instanceof DirectColorModel) {
            DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
                    data.setPixel(x, y, pixel);
                    
                    if (colorModel.hasAlpha()) {
                        data.setAlpha(x, y, (rgb >> 24) & 0xFF);
                    }
                }
            }
            return data;
            
        } else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
            IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
            
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
            }
            
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            WritableRaster braster = bufferedImage.getRaster();
            
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    braster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }
}
