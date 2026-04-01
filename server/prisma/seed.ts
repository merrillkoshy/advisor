import { PrismaPg } from '@prisma/adapter-pg';
import { PrismaClient } from '@prisma/client';
import 'dotenv/config';
import {
  seedDroneComponents,
  seedOverlordClasses,
} from 'prisma/seeds/drone_overlord';
import { seedHeroes } from 'prisma/seeds/heroes';
import { seedStatKeys } from 'prisma/seeds/statKeys';

const adapter = new PrismaPg({
  connectionString: process.env.DATABASE_URL,
});

const prisma = new PrismaClient({ adapter });

async function main() {
  await seedStatKeys();
  await seedHeroes();
  await seedDroneComponents();
  await seedOverlordClasses();
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
