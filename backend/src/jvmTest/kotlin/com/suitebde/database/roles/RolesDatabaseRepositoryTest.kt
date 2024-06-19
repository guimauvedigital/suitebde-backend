package com.suitebde.database.roles

import com.suitebde.database.Database
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.models.UUID
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class RolesDatabaseRepositoryTest {

    @Test
    fun getRolesInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getRolesInAssociation")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val roleFromDatabase = repository.list(UUID())
        assertEquals(1, roleFromDatabase.size)
        assertEquals(roleFromDatabase.first().id, role.id)
        assertEquals(roleFromDatabase.first().associationId, role.associationId)
        assertEquals(roleFromDatabase.first().name, role.name)
    }

    @Test
    fun getRolesInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getRolesInAssociationNotInAssociation")
        val repository = RolesDatabaseRepository(database)
        repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val roleFromDatabase = repository.list(UUID())
        assertEquals(0, roleFromDatabase.size)
    }

    @Test
    fun createRole() = runBlocking {
        val database = Database(protocol = "h2", name = "createRole")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        )
        val rolesFromDatabase = database.suspendedTransaction {
            Roles.selectAll().map(Roles::toRole).singleOrNull()
        }
        assertEquals(rolesFromDatabase?.id, role?.id)
        assertEquals(rolesFromDatabase?.associationId, role?.associationId)
        assertEquals(rolesFromDatabase?.name, role?.name)
    }

    @Test
    fun getRole() = runBlocking {
        val database = Database(protocol = "h2", name = "getRole")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val roleFromDatabase = repository.get(role.id, UUID())
        assertEquals(roleFromDatabase?.id, role.id)
        assertEquals(roleFromDatabase?.associationId, role.associationId)
        assertEquals(roleFromDatabase?.name, role.name)
    }

    @Test
    fun getRoleNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getRoleNotInAssociation")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val roleFromDatabase = repository.get(role.id, UUID())
        assertEquals(roleFromDatabase, null)
    }

    @Test
    fun getRoleNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getRoleNotExists")
        val repository = RolesDatabaseRepository(database)
        val roleFromDatabase = repository.get(UUID(), UUID())
        assertEquals(roleFromDatabase, null)
    }

    @Test
    fun updateRole() = runBlocking {
        val database = Database(protocol = "h2", name = "updateRole")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val payload = UpdateRolePayload("newName")
        assertEquals(true, repository.update(role.id, payload, UUID()))
        val roleFromDatabase = repository.get(role.id, UUID())
        assertEquals(roleFromDatabase?.name, "newName")
    }

    @Test
    fun updateRoleNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateRoleNotInAssociation")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        val payload = UpdateRolePayload("newName")
        assertEquals(false, repository.update(role.id, payload, UUID()))
    }

    @Test
    fun updateRoleNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateRoleNotExists")
        val repository = RolesDatabaseRepository(database)
        val payload = UpdateRolePayload("newName")
        assertEquals(false, repository.update(UUID(), payload, UUID()))
    }

    @Test
    fun deleteRole() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteRole")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        assertEquals(true, repository.delete(role.id, UUID()))
        val roleFromDatabase = repository.get(role.id, UUID())
        assertEquals(roleFromDatabase, null)
    }

    @Test
    fun deleteRoleNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteRoleNotInAssociation")
        val repository = RolesDatabaseRepository(database)
        val role = repository.create(
            CreateRolePayload(
                "name"
            ), UUID()
        ) ?: fail("Unable to create role")
        assertEquals(false, repository.delete(role.id, UUID()))
    }

    @Test
    fun deleteRoleNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteRoleNotExists")
        val repository = RolesDatabaseRepository(database)
        assertEquals(false, repository.delete(UUID(), UUID()))
    }

}
