package org.mconf.bbb.android.test;

import org.mconf.bbb.android.Client;
import org.mconf.bbb.android.Contact;
import org.mconf.bbb.android.ContactAdapter;
import org.mconf.bbb.android.CustomListview;
import org.mconf.bbb.android.ListenerAdapter;
import org.mconf.bbb.android.ListenerContact;
import org.mconf.bbb.android.LoginPage;
import org.mconf.bbb.android.R;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class TestClientSound extends ActivityInstrumentationTestCase2<LoginPage>  {

	private Solo solo;
	private static int LINE_NUMBER=1;
	
	public TestClientSound() {
		super("org.mconf.bbb.android", LoginPage.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.solo = new Solo(getInstrumentation(), getActivity());
		loginAsModerator();
	}

	@Override
	protected void tearDown() throws Exception{
		try {
			this.solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
		
		TestLogin.removeContactsFromMeeting();
	}

	public	void loginAsModerator()
	{
		TestLogin.connectOnMeeting(solo, 0);
	}

	public void loginAsViewer()
	{
		TestLogin.connectOnMeeting(solo, 1);
	}

	public void testStartVoice()
	{
		startVoice();
	}

	public void startVoice()
	{
		
		solo.assertCurrentActivity("didn't go to client", Client.class);
		solo.clickOnMenuItem(solo.getString(R.string.start_voice));
		assertTrue(solo.searchButton(solo.getString(R.string.taptospeak)));
		assertTrue(solo.searchText(TestLogin.NAME, 2));
	}

	public void KickListener()
	{
		//TODO make user i put on the conference have audio
		startVoice();

		String name = getListenerName(solo, LINE_NUMBER);
		solo.clickLongOnText(name);
		solo.clickOnText(solo.getString(R.string.kick));
		assertFalse(solo.searchText(name));
	}

	public void muteListener()
	{
		startVoice();
		assertFalse(isListenerMuted(LINE_NUMBER, solo));
		String name = getListenerName(solo, LINE_NUMBER);
		solo.clickLongOnText(name);
		solo.clickOnText(solo.getString(R.string.mute));
		assertTrue(isListenerMuted(LINE_NUMBER, solo));
	}

	public void tapToSpeak(){
		//TODO como testar?
	}

	public void voiceButton()
	{
		startVoice();
		solo.clickOnButton(solo.getString(R.string.lockspeak));	
		//TODO como testar?
	}

	public void menuSpeaker()
	{
		startVoice();
		solo.clickOnMenuItem(solo.getString(R.string.speaker));
		//TODO como testar?
	}

	public void testMenuStopVoice()
	{
		startVoice();
		solo.clickOnMenuItem(solo.getString(R.string.stop_voice));
		solo.waitForText(solo.getString(R.string.connection_closed));
		assertFalse(solo.searchText(TestLogin.NAME, 2));
		
	}

	public void menuMicGain()
	{
		startVoice();
		solo.clickOnMenuItem(solo.getString(R.string.audio_config));
		solo.waitForText(solo.getString(R.string.audio_config));
		//como mudar os par�metros?
		//TODO como testar?
	}

	public void menuEarphone()
	{
		startVoice();
		solo.clickOnMenuItem(solo.getString(R.string.audio_config));
		solo.waitForText(solo.getString(R.string.audio_config));
		//como mudar os par�metros?
		//TODO como testar?
	}

	public void menuSpeakerSound()
	{
		startVoice();
		solo.clickOnMenuItem(solo.getString(R.string.audio_config));
		solo.waitForText(solo.getString(R.string.audio_config));
		//como mudar os par�metros?
		//TODO como testar?
	}
	
	public static String getListenerName(Solo solo, int num)
	{
		solo.waitForText(solo.getString(R.string.list_listeners));
		CustomListview listeners = (CustomListview) solo.getView(R.id.listeners_list);
		ListenerAdapter listAdapter = (ListenerAdapter) listeners.getAdapter();
		ListenerContact listener = listAdapter.getListener(num-1);
		return listener.getListenerName();
	}
	
	public  static boolean isListenerMuted(int num, Solo solo)
	{
		solo.waitForText(solo.getString(R.string.list_listeners));
		CustomListview listeners = (CustomListview) solo.getView(R.id.listeners_list);
		ListenerAdapter listenerAdapter = (ListenerAdapter) listeners.getAdapter();
		ListenerContact listener = listenerAdapter.getListener(num-1);
		return listener.isMuted();
	}
}