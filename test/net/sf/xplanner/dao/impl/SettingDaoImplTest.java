package net.sf.xplanner.dao.impl;

import net.sf.xplanner.dao.ObjectTypeDao;
import net.sf.xplanner.dao.SettingDao;
import net.sf.xplanner.domain.ObjectType;
import net.sf.xplanner.domain.Setting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class SettingDaoImplTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-text.xml"})
public class SettingDaoImplTest {
    
    /** The setting dao. */
    @Autowired
    private SettingDao settingDao;
    
    /** The object type dao. */
    @Autowired
    private ObjectTypeDao objectTypeDao;

    /**
     * Test save setting.
     */
    @Test
    public void testSaveSetting() {
        ObjectType objectType = new ObjectType();
        objectType.setName("project");
        objectTypeDao.save(objectType);
        Setting setting = new Setting();
        setting.setName("test");
        setting.setObjectType(objectType);
        settingDao.save(setting);
        System.out.println(setting.getId());
        settingDao.delete(setting);
        objectTypeDao.delete(objectType.getId());
    }
    
}
