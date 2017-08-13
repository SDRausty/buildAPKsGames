/*
Copyright (C) 2013 - 2014 Braden Walters

This software may be modified and distributed under the terms of the MIT
license. See the LICENSE file for details.
*/

package info.meoblast001.thugaim;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import info.meoblast001.thugaim.engine.*;
import info.meoblast001.thugaim.npc.*;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParserException;

/**
Top level of game code. Manages all gameplay elements either directly or
indirectly.
*/
public class ThugaimRuntime implements IGameRuntime
{
  /**
  Exception thrown if levels cannot be loaded.
  */
  public static class LoadLevelsException extends Exception
  {
    // No additional functionality.
  }

  private static final long SHOW_LEVEL_COMPLETE_BEFORE_END_MILIS = 3000;
  public static final int CHECKPOINT_INTERVAL = 2;

  private Engine engine;
  private Context context;

  private World world;
  private StationGraph station_graph;
  private Player player;
  private HealthBar health_bar;
  private boolean player_won = false, player_lost = false;
  private long started_level_complete_millis = Long.MAX_VALUE;

  //Level information.
  private static int current_level = 0;
  private static Vector<LevelDescriptor> levels = null;

  //Checkpoint information.
  private static int checkpoint_level = 0;

  //Music enabled status.
  private static boolean music_enabled = true;

  /**
  Constructs game runtime. Failure is fatal.
  @throws LoadLevelsException If levels cannot be loaded.
  */
  public ThugaimRuntime(Resources resources) throws LoadLevelsException
  {
    if (levels == null)
      loadLevels(resources);
  }

  public void init(Engine engine)
  {
    this.engine = engine;
    context = engine.getGraphics().getContext();

    //Get the level descriptor for this level.
    LevelDescriptor level = getCurrentLevelDescriptor();

    //Only show what's in the play area.
    int half_play_size = level.getPlaySize() / 2;
    engine.getGraphics().enableClip(-half_play_size, half_play_size,
                                    half_play_size, -half_play_size);

    world = new World(engine, level.getPlaySize());
    station_graph = new StationGraph(engine, world, level.getStations(),
        level.getPlaySize());

    player = new Player(engine, station_graph);
    world.insertActor(player);
    world.focusOnActor("player");

    HydrogenFighter.generateAll(engine, world, level.getPlaySize(),
                                station_graph, level.getHydrogenFighters());
    HeliumFighter.generateAll(engine, world, level.getPlaySize(), station_graph,
                              level.getHeliumFighters());

    health_bar = new HealthBar(engine.getGraphics(), player);
    PlayAreaShield.generateAll(engine, world, level.getPlaySize());

    //Start music if it exists and the user selected music to be played, else
    //stop any currently playing music.
    if (level.getMusic() != null && music_enabled)
    {
      int res_id = context.getResources().getIdentifier(level.getMusic(), "raw",
        context.getPackageName());
      engine.getAudio().startMusic(res_id);
    }
    else
      engine.getAudio().stopMusic();
  }

  public void update(long millisecond_delta, float rotation, boolean tapped)
  {
    station_graph.update();
    world.update(millisecond_delta, rotation, tapped);
    health_bar.update();
    displayLevelNumber();

    //Player won if there are no stations remaining and the player didn't
    //already lose.
    if (station_graph.getStations().length == 0 && !player_lost)
      player_won = true;
    //Player lost if it's no longer in the world and has not already won.
    if (player.getWorld() == null && !player_won)
      player_lost = true;

    if (player_won)
      displayLevelComplete();
  }

  public boolean isRunning()
  {
    return !((player_won && System.currentTimeMillis() -
      started_level_complete_millis > SHOW_LEVEL_COMPLETE_BEFORE_END_MILIS) ||
      player_lost);
  }

  public boolean didPlayerWin()
  {
    return player_won;
  }

  /**
  Does a level proceed the current level?
  @return True if yes, false if no.
  */
  public boolean hasNextLevel()
  {
    return current_level + 1 < levels.size();
  }

  /**
  Select level to use.
  @param current_level 0-based level number.
  @return True if level exists, else level does not exist.
  */
  public boolean setLevel(int current_level)
  {
    if (current_level < levels.size())
    {
      this.current_level = current_level;
      //If the level is higher than the last checkpoint and is a checkpoint
      //itself, set this checkpoint as reached.
      if (current_level > checkpoint_level &&
          (current_level + 1) % CHECKPOINT_INTERVAL == 0)
        checkpoint_level = current_level;
      return true;
    }
    else
      return false;
  }

  /**
  Return the current level descriptor.
  @return Level descriptor
  */
  public LevelDescriptor getCurrentLevelDescriptor()
  {
    return levels.get(current_level);
  }

  /**
  Return the current checkpoint. This is the level to which the game returns
  after game over.
  @return The checkpoint.
  */
  public static int getCheckpointLevel()
  {
    return checkpoint_level;
  }

  /**
  Load all of the level descriptions from the XML file.
  @param resources Activity's resources.
  @throws LoadLevelsException If levels file cannot be loaded.
  */
  private static boolean loadLevels(Resources resources) throws
    LoadLevelsException
  {
    try
    {
      levels = new Vector<LevelDescriptor>();
      XmlResourceParser xml_parser = resources.getXml(R.xml.levels);
      int event_type = xml_parser.getEventType();
      while (event_type != XmlResourceParser.END_DOCUMENT)
      {
        if (event_type == XmlResourceParser.START_TAG &&
            xml_parser.getName().equals("level"))
        {
          LevelDescriptor level = new LevelDescriptor();

          for (int i = 0; i < xml_parser.getAttributeCount(); ++i)
          {
            String attr_name = xml_parser.getAttributeName(i);
            String attr_value = xml_parser.getAttributeValue(i);

            if (attr_name.equals("music"))
              level.setMusic(attr_value);
            else if (attr_name.equals("stations"))
              level.setStations(Integer.parseInt(attr_value));
            else if (attr_name.equals("hydrogen_fighters"))
              level.setHydrogenFighters(Integer.parseInt(attr_value));
            else if (attr_name.equals("helium_fighters"))
              level.setHeliumFighters(Integer.parseInt(attr_value));
            else if (attr_name.equals("play_size"))
              level.setPlaySize(Integer.parseInt(attr_value));
            //Else ignore this attribute.
          }

          levels.add(level);
        }
        event_type = xml_parser.next();
      }

      return true;
    }
    catch (XmlPullParserException e)
    {
      throw new LoadLevelsException();
    }
    catch (IOException e)
    {
      throw new LoadLevelsException();
    }
    catch (NumberFormatException e)
    {
      throw new LoadLevelsException();
    }
  }

  /**
  Set whether music will be played in future initialised runtimes.
  @param enabled True if music should be played, else false.
  */
  public static void setMusicEnabled(boolean enabled)
  {
    music_enabled = enabled;
  }

  /**
  Displays the current level at the bottom-left of the screen.
  */
  private void displayLevelNumber()
  {
    Paint fill = new Paint();
    fill.setColor(Color.BLACK);
    Paint stroke = new Paint();
    stroke.setColor(Color.WHITE);
    Graphics graphics = engine.getGraphics();
    graphics.drawTextHud(context.getString(R.string.level_number_indicator,
                                           current_level + 1),
                         10, graphics.getHeight() - 10, 30.0f,
                         Paint.Align.LEFT, fill, stroke);
  }

  /**
  Displays the level complete screen. If called first time, sets the timer to
  end the game.
  */
  private void displayLevelComplete()
  {
    if (started_level_complete_millis == Long.MAX_VALUE)
      started_level_complete_millis = System.currentTimeMillis();

    Paint fill = new Paint();
    fill.setARGB(255, 0, 100, 0);
    Paint stroke = new Paint();
    stroke.setColor(Color.WHITE);
    Graphics graphics = engine.getGraphics();
    graphics.drawTextHud(context.getString(R.string.level_complete),
                         graphics.getWidth() / 2, graphics.getHeight() / 2,
                         30.0f, Paint.Align.CENTER, fill, stroke);
  }
}
