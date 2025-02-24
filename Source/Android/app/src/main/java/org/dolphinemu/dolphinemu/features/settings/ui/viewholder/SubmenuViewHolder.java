// SPDX-License-Identifier: GPL-2.0-or-later

package org.dolphinemu.dolphinemu.features.settings.ui.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.dolphinemu.dolphinemu.R;
import org.dolphinemu.dolphinemu.features.settings.model.view.SettingsItem;
import org.dolphinemu.dolphinemu.features.settings.model.view.SubmenuSetting;
import org.dolphinemu.dolphinemu.features.settings.ui.SettingsAdapter;

public final class SubmenuViewHolder extends SettingViewHolder
{
  private SubmenuSetting mItem;

  private TextView mTextSettingName;
  private TextView mTextSettingDescription;

  public SubmenuViewHolder(View itemView, SettingsAdapter adapter)
  {
    super(itemView, adapter);
  }

  @Override
  protected void findViews(View root)
  {
    mTextSettingName = root.findViewById(R.id.text_setting_name);
    mTextSettingDescription = root.findViewById(R.id.text_setting_description);
  }

  @Override
  public void bind(SettingsItem item)
  {
    mItem = (SubmenuSetting) item;

    mTextSettingName.setText(item.getName());

    if (!TextUtils.isEmpty(item.getDescription()))
    {
      mTextSettingDescription.setText(item.getDescription());
    }
  }

  @Override
  public void onClick(View clicked)
  {
    getAdapter().onSubmenuClick(mItem);
  }

  @Nullable @Override
  protected SettingsItem getItem()
  {
    return mItem;
  }
}
