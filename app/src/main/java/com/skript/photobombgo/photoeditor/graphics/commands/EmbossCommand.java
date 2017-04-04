package com.skript.photobombgo.photoeditor.graphics.commands;

import android.graphics.Bitmap;

import com.skript.photobombgo.photoeditor.graphics.ConvolutionMatrix;

public class EmbossCommand implements ImageProcessingCommand {

	private static final String ID = "skript.com.pokemontattoo.photoeditor.graphics.commands.EmbossCommand";

	private double[][] config = new double[][] {
			{ -2, -1, 0 },
			{ -1, 1, 1 }, 
			{ 0, 1, 2 } };

	public EmbossCommand() {
	}

	public Bitmap process(Bitmap bitmap) {
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(config);
		convMatrix.Factor = 1;
		convMatrix.Offset = 0;
		return ConvolutionMatrix.computeConvolution(bitmap, convMatrix);
	}

	public String getId() {
		return ID;
	}

}
