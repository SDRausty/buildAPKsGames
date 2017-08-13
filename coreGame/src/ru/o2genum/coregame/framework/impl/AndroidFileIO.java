package ru.o2genum.coregame.framework.impl;

import java.io.*;

import android.content.res.AssetManager;
import android.os.Environment;

import ru.o2genum.coregame.framework.FileIO;

public class AndroidFileIO implements FileIO
{
	AssetManager assets;
	String externalStoragePath;

	public AndroidFileIO(AssetManager assets)
	{
		this.assets = assets;
		this.externalStoragePath = 
			Environment.getExternalStorageDirectory().getAbsolutePath() +
			File.separator;
	}

	@Override
	public InputStream readAsset(String fileName) throws IOException
	{
		return assets.open(fileName);
	}

	@Override
	public InputStream readFile(String fileName) throws IOException
	{
		return new FileInputStream(externalStoragePath + fileName);
	}

	@Override
	public OutputStream writeFile(String fileName) throws IOException
	{
		return new FileOutputStream(externalStoragePath + fileName);
	}
}
