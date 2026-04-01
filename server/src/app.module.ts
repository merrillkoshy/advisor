import { Module } from '@nestjs/common';
import { PrismaModule } from 'prisma/prisma.module';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { DroneModule } from './drone/drone.module';
import { HeroesModule } from './heroes/heroes.module';
import { OverlordModule } from './overlord/overlord.module';

@Module({
  imports: [PrismaModule, HeroesModule, DroneModule, OverlordModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
