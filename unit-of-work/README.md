---
title: Unit Of Work
category: Data access
language: en
tag:
    - Data access
    - Decoupling
    - Persistence
    - Transactions
---

## Intent

The Unit of Work pattern maintains a list of objects affected by a business transaction and coordinates the writing out of changes and the resolution of concurrency problems.

## Explanation

Real-world example

> Imagine a library where a librarian tracks all the books that are borrowed and returned throughout the day. Instead of updating the library's inventory system every time a single transaction occurs, the librarian keeps a list of all the changes and updates the system once at the end of the day. This approach ensures that all changes are processed together, maintaining the integrity of the inventory and reducing the number of individual updates needed. This is analogous to the Unit of Work pattern in software, where all changes to a set of objects are tracked and committed as a single transaction to maintain consistency and efficiency. 

In plain words

> The Unit of Work pattern tracks changes to objects during a transaction and commits all changes as a single unit to ensure consistency and efficiency. 

[MartinFowler.com](https://martinfowler.com/eaaCatalog/unitOfWork.html) says

> Maintains a list of objects affected by a business transaction and coordinates the writing out of changes and the resolution of concurrency problems.

**Programmatic Example**

Arms dealer has a database containing weapon information. Merchants all over the town are constantly updating this information causing a high load on the database server. To make the load more manageable we apply to Unit of Work pattern to send many small updates in batches.

Here's the `Weapon` entity that is being persisted in the database.

```java
@Getter
@RequiredArgsConstructor
public class Weapon {
    private final Integer id;
    private final String name;
}
```

The essence of the implementation is the `ArmsDealer` implementing the Unit of Work pattern. It maintains a map of database operations (`context`) that need to be done and when `commit` is called it applies them in a single batch.

```java
public interface IUnitOfWork<T> {
    
  String INSERT = "INSERT";
  String DELETE = "DELETE";
  String MODIFY = "MODIFY";

  void registerNew(T entity);

  void registerModified(T entity);

  void registerDeleted(T entity);

  void commit();
}

@Slf4j
@RequiredArgsConstructor
public class ArmsDealer implements IUnitOfWork<Weapon> {

    private final Map<String, List<Weapon>> context;
    private final WeaponDatabase weaponDatabase;

    @Override
    public void registerNew(Weapon weapon) {
        LOGGER.info("Registering {} for insert in context.", weapon.getName());
        register(weapon, UnitActions.INSERT.getActionValue());
    }

    @Override
    public void registerModified(Weapon weapon) {
        LOGGER.info("Registering {} for modify in context.", weapon.getName());
        register(weapon, UnitActions.MODIFY.getActionValue());

    }

    @Override
    public void registerDeleted(Weapon weapon) {
        LOGGER.info("Registering {} for delete in context.", weapon.getName());
        register(weapon, UnitActions.DELETE.getActionValue());
    }

    private void register(Weapon weapon, String operation) {
        var weaponsToOperate = context.get(operation);
        if (weaponsToOperate == null) {
            weaponsToOperate = new ArrayList<>();
        }
        weaponsToOperate.add(weapon);
        context.put(operation, weaponsToOperate);
    }

    /**
     * All UnitOfWork operations are batched and executed together on commit only.
     */
    @Override
    public void commit() {
        if (context == null || context.isEmpty()) {
            return;
        }
        LOGGER.info("Commit started");
        if (context.containsKey(UnitActions.INSERT.getActionValue())) {
            commitInsert();
        }

        if (context.containsKey(UnitActions.MODIFY.getActionValue())) {
            commitModify();
        }
        if (context.containsKey(UnitActions.DELETE.getActionValue())) {
            commitDelete();
        }
        LOGGER.info("Commit finished.");
    }

    private void commitInsert() {
        var weaponsToBeInserted = context.get(UnitActions.INSERT.getActionValue());
        for (var weapon : weaponsToBeInserted) {
            LOGGER.info("Inserting a new weapon {} to sales rack.", weapon.getName());
            weaponDatabase.insert(weapon);
        }
    }

    private void commitModify() {
        var modifiedWeapons = context.get(UnitActions.MODIFY.getActionValue());
        for (var weapon : modifiedWeapons) {
            LOGGER.info("Scheduling {} for modification work.", weapon.getName());
            weaponDatabase.modify(weapon);
        }
    }

    private void commitDelete() {
        var deletedWeapons = context.get(UnitActions.DELETE.getActionValue());
        for (var weapon : deletedWeapons) {
            LOGGER.info("Scrapping {}.", weapon.getName());
            weaponDatabase.delete(weapon);
        }
    }
}
```

Here is how the whole app is put together.

```java
// create some weapons
var enchantedHammer = new Weapon(1, "enchanted hammer");
var brokenGreatSword = new Weapon(2, "broken great sword");
var silverTrident = new Weapon(3, "silver trident");

// create repository
var weaponRepository = new ArmsDealer(new HashMap<String, List<Weapon>>(), new WeaponDatabase());

// perform operations on the weapons
weaponRepository.registerNew(enchantedHammer);
weaponRepository.registerModified(silverTrident);
weaponRepository.registerDeleted(brokenGreatSword);
weaponRepository.commit();
```

Here is the console output.

```
21:39:21.984 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Registering enchanted hammer for insert in context.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Registering silver trident for modify in context.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Registering broken great sword for delete in context.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Commit started
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Inserting a new weapon enchanted hammer to sales rack.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Scheduling silver trident for modification work.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Scrapping broken great sword.
21:39:21.989 [main] INFO com.iluwatar.unitofwork.ArmsDealer - Commit finished.
```

## Class diagram

![Unit of Work](./etc/unit-of-work.urm.png "Unit of Work")

## Applicability

* Use when you need to manage multiple operations that need to be treated as a single transaction.
* Ideal in scenarios where changes to the business objects must be tracked and saved in a coordinated manner.
* Useful when working with object-relational mapping (ORM) frameworks in Java such as Hibernate.

## Tutorials

* [Repository and Unit of Work Pattern - Wolfgang Ofner](https://www.programmingwithwolfgang.com/repository-and-unit-of-work-pattern/)
* [Unit of Work - a Design Pattern - Mono](https://mono.software/2017/01/13/unit-of-work-a-design-pattern/)

## Known Uses

* Implementations in Java-based ORM frameworks like Hibernate.
* Enterprise applications where multiple database operations need to be atomic.
* Complex transactional systems where multiple objects are modified and persisted together.

## Consequences

Benefits:

* Ensures data integrity by managing transactions effectively.
* Reduces the number of database calls by batching them together.
* Simplifies the persistence logic by decoupling transaction management from the business logic.

Trade-offs:

* Can introduce complexity in managing the life cycle of objects within the unit of work.
* Potential performance overhead if not managed properly, especially with large datasets.

## Related Patterns

* [Identity Map](https://java-design-patterns.com/patterns/identity-map/): Helps to ensure that each object is only loaded once per transaction, reducing redundancy and improving performance.
* [Repository](https://java-design-patterns.com/patterns/repository/): Often used in conjunction with Unit of Work to abstract the persistence logic and provide a cleaner way to access data.
* [Transaction Script](https://java-design-patterns.com/patterns/transaction-script/): While different in its procedural approach, it can complement Unit of Work by managing transactional logic at a higher level.

## Credits

* [Domain-Driven Design: Tackling Complexity in the Heart of Software](https://amzn.to/3wlDrze)
* [Java Persistence with Hibernate](https://amzn.to/44tP1ox)
* [Patterns of Enterprise Application Architecture](https://amzn.to/3WfKBPR)
* [Unit Of Work Design Pattern - Code Project](https://www.codeproject.com/Articles/581487/Unit-of-Work-Design-Pattern)
* [Unit Of Work - Martin Fowler](https://martinfowler.com/eaaCatalog/unitOfWork.html)
