package org.smartregister.chw.dao;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.domain.Person;
import org.smartregister.chw.domain.PncBaby;
import org.smartregister.repository.Repository;

import java.util.List;

public class PersonDaoTest extends PersonDao {
    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }

    @Test
    public void testGetMothersChildren() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "base_entity_id", "first_name", "last_name", "middle_name", "dob"});
        matrixCursor.addRow(new Object[]{"base_entity_id", "first_name", "last_name", "middle_name", "dob"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        List<Person> personList = PersonDao.getMothersChildren(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertEquals(personList.get(0).getBaseEntityID(), "base_entity_id");
        Assert.assertEquals(personList.get(0).getFirstName(), "First_name");
        Assert.assertEquals(personList.get(0).getLastName(), "Last_name");
        Assert.assertEquals(personList.get(0).getMiddleName(), "Middle_name");
    }

    @Test
    public void testGetMothersPNCBabies() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "base_entity_id", "first_name", "last_name", "middle_name", "dob", "low_birth_weight"});
        matrixCursor.addRow(new Object[]{"base_entity_id", "first_name", "last_name", "middle_name", "dob", "low_birth_weight"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        List<PncBaby> personList = PersonDao.getMothersPNCBabies(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertEquals(personList.get(0).getBaseEntityID(), "base_entity_id");
        Assert.assertEquals(personList.get(0).getFirstName(), "First_name");
        Assert.assertEquals(personList.get(0).getLastName(), "Last_name");
        Assert.assertEquals(personList.get(0).getMiddleName(), "Middle_name");
    }

    @Test
    public void testGetMothersPNCBabiesReturnsEmptyArrayList() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "base_entity_id", "first_name", "last_name", "middle_name", "dob", "low_birth_weight"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        List<PncBaby> personList = PersonDao.getMothersPNCBabies(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertEquals(personList.size(), 0);
    }

    @Test
    public void getAncCreatedDate() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "date_created"});
        matrixCursor.addRow(new Object[]{"date_created"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        String dateCreated = PersonDao.getAncCreatedDate(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertEquals(dateCreated, "date_created");
    }

    @Test
    public void getAncCreatedDateReturnsNull() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "date_created"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        String dateCreated = PersonDao.getAncCreatedDate(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNull(dateCreated);
    }

    @Test
    public void getDob() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "dob"});
        matrixCursor.addRow(new Object[]{"dob"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());

        String baseEntityId = "1234";
        String dateCreated = PersonDao.getDob(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertEquals(dateCreated, "dob");
    }

    @Test
    public void getDobReturnsNull() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                "dob"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        String baseEntityId = "1234";
        String dateCreated = PersonDao.getDob(baseEntityId);
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNull(dateCreated);
    }
}