// SPDX-License-Identifier: GPL-2.0-or-later

package org.dolphinemu.dolphinemu.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import org.dolphinemu.dolphinemu.NativeLibrary;
import org.dolphinemu.dolphinemu.R;
import org.dolphinemu.dolphinemu.model.GameFile;
import org.dolphinemu.dolphinemu.services.GameFileCacheManager;
import org.dolphinemu.dolphinemu.utils.GameBannerRequestHandler;

public final class GameDetailsDialog extends DialogFragment
{
  private static final String ARG_GAME_PATH = "game_path";

  public static GameDetailsDialog newInstance(String gamePath)
  {
    GameDetailsDialog fragment = new GameDetailsDialog();

    Bundle arguments = new Bundle();
    arguments.putString(ARG_GAME_PATH, gamePath);
    fragment.setArguments(arguments);

    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    GameFile gameFile = GameFileCacheManager.addOrGet(getArguments().getString(ARG_GAME_PATH));

    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),
            R.style.DolphinDialogBase);
    ViewGroup contents = (ViewGroup) getActivity().getLayoutInflater()
            .inflate(R.layout.dialog_game_details, null);

    ImageView banner = contents.findViewById(R.id.banner);

    TextView textTitle = contents.findViewById(R.id.text_game_title);
    TextView textDescription = contents.findViewById(R.id.text_description);

    TextView textCountry = contents.findViewById(R.id.text_country);
    TextView textCompany = contents.findViewById(R.id.text_company);
    TextView textGameId = contents.findViewById(R.id.text_game_id);
    TextView textRevision = contents.findViewById(R.id.text_revision);

    TextView textFileFormat = contents.findViewById(R.id.text_file_format);
    TextView textCompression = contents.findViewById(R.id.text_compression);
    TextView textBlockSize = contents.findViewById(R.id.text_block_size);

    TextView labelFileFormat = contents.findViewById(R.id.label_file_format);
    TextView labelCompression = contents.findViewById(R.id.label_compression);
    TextView labelBlockSize = contents.findViewById(R.id.label_block_size);

    String country = getResources().getStringArray(R.array.countryNames)[gameFile.getCountry()];
    String description = gameFile.getDescription();
    String fileSize = NativeLibrary.FormatSize(gameFile.getFileSize(), 2);

    textTitle.setText(gameFile.getTitle());
    textDescription.setText(gameFile.getDescription());
    if (description.isEmpty())
    {
      textDescription.setVisibility(View.GONE);
    }

    textCountry.setText(country);
    textCompany.setText(gameFile.getCompany());
    textGameId.setText(gameFile.getGameId());
    textRevision.setText(String.valueOf(gameFile.getRevision()));

    if (!gameFile.shouldShowFileFormatDetails())
    {
      labelFileFormat.setText(R.string.game_details_file_size);
      textFileFormat.setText(fileSize);

      labelCompression.setVisibility(View.GONE);
      textCompression.setVisibility(View.GONE);
      labelBlockSize.setVisibility(View.GONE);
      textBlockSize.setVisibility(View.GONE);
    }
    else
    {
      long blockSize = gameFile.getBlockSize();
      String compression = gameFile.getCompressionMethod();

      textFileFormat.setText(getResources().getString(R.string.game_details_size_and_format,
              gameFile.getFileFormatName(), fileSize));

      if (compression.isEmpty())
      {
        textCompression.setText(R.string.game_details_no_compression);
      }
      else
      {
        textCompression.setText(gameFile.getCompressionMethod());
      }

      if (blockSize > 0)
      {
        textBlockSize.setText(NativeLibrary.FormatSize(blockSize, 0));
      }
      else
      {
        labelBlockSize.setVisibility(View.GONE);
        textBlockSize.setVisibility(View.GONE);
      }
    }

    loadGameBanner(banner, gameFile);

    builder.setView(contents);
    return builder.create();
  }

  private static void loadGameBanner(ImageView imageView, GameFile gameFile)
  {
    Picasso picassoInstance = new Picasso.Builder(imageView.getContext())
            .addRequestHandler(new GameBannerRequestHandler(gameFile))
            .build();

    picassoInstance
            .load(Uri.parse("iso:/" + gameFile.getPath()))
            .fit()
            .noFade()
            .noPlaceholder()
            .config(Bitmap.Config.RGB_565)
            .error(R.drawable.no_banner)
            .into(imageView);
  }
}
