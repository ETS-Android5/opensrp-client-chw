package org.smartregister.chw.activity;

import android.content.Intent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.chw.BaseUnitTest;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.core.activity.CoreAncMedicalHistoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rkodev
 */
public class AncMedicalHistoryActivityTest extends BaseUnitTest {

    protected AncMedicalHistoryActivity activity;
    protected ActivityController<AncMedicalHistoryActivity> controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(AncMedicalHistoryActivity.class);
        activity = controller.get();

        activity = Mockito.spy(activity);
        // mute this presenter
        Mockito.doNothing().when(activity).setUpView();
        Mockito.doNothing().when(activity).initializePresenter();
    }

    @Test
    public void testStartMe() {
        Mockito.doNothing().when(activity).startActivity(Mockito.any(Intent.class));
        MemberObject memberObject = Mockito.mock(MemberObject.class);
        AncMedicalHistoryActivity.startMe(activity, memberObject);

        ArgumentCaptor<Intent> intentArgumentCaptor = ArgumentCaptor.forClass(Intent.class);

        Mockito.verify(activity).startActivity(intentArgumentCaptor.capture());
        Assert.assertEquals(intentArgumentCaptor.getValue().getSerializableExtra("MemberObject"), memberObject);
    }

    @Test
    public void testRenderView() {
        Mockito.doNothing().when(activity).displayLoadingState(Mockito.anyBoolean());
        CoreAncMedicalHistoryActivity.Flavor flavor = Mockito.mock(CoreAncMedicalHistoryActivity.Flavor.class);
        ReflectionHelpers.setField(activity, "flavor", flavor);
        List<Visit> visits = new ArrayList<>();

        ArgumentCaptor<List<Visit>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        activity.renderView(visits);
        Mockito.verify(flavor).processViewData(listArgumentCaptor.capture(), Mockito.eq(activity));
        Assert.assertEquals(listArgumentCaptor.getValue(), visits);
    }

    @After
    public void tearDown() {
        try {
            activity.finish();
            controller.pause().stop().destroy(); //destroy controller if we can
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
