package org.smartregister.chw;

import org.junit.runner.RunWith;
import org.koin.test.AutoCloseKoinTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.chw.application.TestChwApplication;
import org.smartregister.chw.shadows.BaseJobShadow;
import org.smartregister.chw.shadows.ContextShadow;
import org.smartregister.chw.shadows.CustomFontTextViewShadow;
import org.smartregister.chw.shadows.KujakuMapViewShadow;

/**
 * Created by keyman on 11/03/2019.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = TestChwApplication.class, shadows = {ContextShadow.class,
        BaseJobShadow.class, CustomFontTextViewShadow.class,
        KujakuMapViewShadow.class})
public abstract class BaseUnitTest extends AutoCloseKoinTest {

}
