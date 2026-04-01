import { Controller, Get } from '@nestjs/common';
import { OverlordService } from './overlord.service';

@Controller('overlord')
export class OverlordController {
  constructor(private readonly overlordService: OverlordService) {}

  @Get('classes')
  findAllClasses() {
    return this.overlordService.findAllClasses();
  }
}
